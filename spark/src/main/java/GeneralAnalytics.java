import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.collect_list;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.current_timestamp;
import static org.apache.spark.sql.functions.dayofweek;
import static org.apache.spark.sql.functions.desc;
import static org.apache.spark.sql.functions.first;
import static org.apache.spark.sql.functions.hour;
import static org.apache.spark.sql.functions.lag;
import static org.apache.spark.sql.functions.lead;
import static org.apache.spark.sql.functions.struct;
import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.timestamp_seconds;
import static org.apache.spark.sql.functions.to_date;
import static org.apache.spark.sql.functions.to_utc_timestamp;
import static org.apache.spark.sql.functions.unix_timestamp;
import static org.apache.spark.sql.functions.when;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

@Slf4j
public class GeneralAnalytics {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkUtils.createSparkSession();

        // Dataset<Row> loginRows = MongoConnector.read(spark, "login");
        // Dataset<Row> result = loginRows.groupBy("account._id").count().sort("count");
        // MongoConnector.write(result, "loginCount");

        MongoConnector mongoConnector = new MongoConnector(spark);

        NullableDatasetRow biasRows = new NullableDatasetRow(mongoConnector.readRecent("bias"));
        NullableDatasetRow multiRows =  new NullableDatasetRow(mongoConnector.readRecent("multi"));
        NullableDatasetRow promoteRows =  new NullableDatasetRow(mongoConnector.readRecent("promote"));
        NullableDatasetRow autoPromoteRows =  new NullableDatasetRow(mongoConnector.readRecent("autoPromote"));
        NullableDatasetRow throwVinegarRows =  new NullableDatasetRow(mongoConnector.readRecent("throwVinegar"));

        Dataset<Row> actionByAccount = biasRows.select("ranker.account", "createdOn")
            .union(multiRows.select("ranker.account", "createdOn"))
            .union(promoteRows.select("ranker.account", "createdOn"))
            .union(autoPromoteRows.select("ranker.account", "createdOn"))
            .union(throwVinegarRows.select("ranker.account", "createdOn"))
            .getDataset().withColumnRenamed("ranker.account", "account");

        WindowSpec window = Window.partitionBy("account").orderBy("createdOn");
        Column createdOnColumn = unix_timestamp(col("createdOn"));
        Column nextCreatedOnColumn = unix_timestamp(lead(col("createdOn"), 1).over(window));
        Column differenceInColumns = nextCreatedOnColumn.minus(createdOnColumn);

        Dataset<Row> accountActionsWithDiff = actionByAccount
            .withColumn("diff",
                // When the createdOn are closer than 30 minutes, then the difference is added in
                // the diff column, otherwise we add a minute of activity
                when(differenceInColumns.leq(1800), differenceInColumns).otherwise(60)
            )
            .withColumn("hour", hour(col("createdOn")))
            .withColumn("weekday", dayofweek(col("createdOn")))
            .withColumn("date", to_date(col("createdOn")));

        Dataset<Row> accountActivityInSeconds = accountActionsWithDiff
            .groupBy("account").sum("diff")
            .withColumnRenamed("sum(diff)", "activityInSec")
            .sort(desc("activityInSec"));

        Dataset<Row> timePerHour = accountActionsWithDiff.groupBy("hour", "date")
            .agg(sum("diff").as("totalSeconds"));

        timePerHour.show(100);

        timePerHour = timePerHour.groupBy("hour").agg(avg("totalSeconds")).as("avgSeconds")
            .orderBy(col("hour"));

        timePerHour.show(100);

        Dataset<Row> timePerWeekday = accountActionsWithDiff.groupBy("weekday")
            .agg(sum("diff").as("totalSeconds"))
            .orderBy(col("weekday"));


        Dataset<Row> analysis = accountActivityInSeconds
            .withColumn("accountActivity", struct(col("*")));
        analysis = analysis.groupBy()
            .agg(
                current_timestamp().alias("createdOn"),
                collect_list("accountActivity").alias("accountActivity")
            );
        analysis = analysis.join(timePerHour
            .withColumn("activityPerHour", struct(timePerHour.col("*")))
            .groupBy().agg(
                collect_list("activityPerHour").alias("activityPerHour")
            )
        );

        // mongoConnector.write(analysis, "generalAnalysis");
    }

}
