package de.kaliburg.morefair.game.message;

import java.time.format.DateTimeFormatter;
import lombok.Data;
import org.apache.commons.text.StringEscapeUtils;

@Data
public class MessageDTO {

  private final String message;
  private final String username;
  private final Integer timesAsshole;
  private final Long accountId;
  private final String timeCreated;
  private final String assholeTag;
  private final String metadata;

  public MessageDTO(MessageEntity message) {
    this.timesAsshole = Math.min(message.getAccount().getTimesAsshole(),
        de.kaliburg.morefair.FairController.ASSHOLE_TAGS.size() - 1);
    this.assholeTag =
        de.kaliburg.morefair.FairController.ASSHOLE_TAGS.get(
            Math.min(message.getAccount().getTimesAsshole(),
                FairController.ASSHOLE_TAGS.size() - 1));
    this.message = StringEscapeUtils.unescapeJava(message.getMessage());
    this.username = StringEscapeUtils.unescapeJava(message.getAccount().getUsername());
    this.accountId = message.getAccount().getId();
    this.timeCreated = message.getCreatedOn().format(DateTimeFormatter.ofPattern("EE HH:mm"));
    this.metadata = message.getMetadata();
  }
}
