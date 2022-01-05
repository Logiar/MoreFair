package de.kaliburg.morefair.service;

import de.kaliburg.morefair.dto.LadderViewDTO;
import de.kaliburg.morefair.entity.Account;
import de.kaliburg.morefair.entity.Ladder;
import de.kaliburg.morefair.entity.Ranker;
import de.kaliburg.morefair.repository.RankerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class RankerService {
    private final static int LADDER_AREA_SIZE = 30;
    private final RankerRepository rankerRepository;
    private final LadderService ladderRepository;

    public RankerService(RankerRepository rankerRepository, LadderService ladderRepository) {
        this.rankerRepository = rankerRepository;
        this.ladderRepository = ladderRepository;
    }

    public List<Ranker> findAll() {
        return rankerRepository.findAll();
    }

    public LadderViewDTO findAllRankerByHighestLadderAreaAndAccount(Account account) {
        Ranker currentRanker = findHighestRankerByAccount(account);
        Ladder currentLadder = currentRanker.getLadder();

        assert (currentRanker.getAccount().getUuid().equals(account.getUuid()));
        assert (currentRanker.getAccount().getUsername().equals(account.getUsername()));

        List<Ranker> result = findAllRankerByLadderArea(currentRanker, currentLadder);
        return new LadderViewDTO(result, currentLadder, account, findHighestPointsByLadder(currentLadder));
    }

    public List<Ranker> findByAccount(Account account) {
        return rankerRepository.findByAccount(account);
    }

    public Ranker findHighestRankerByAccount(Account account) {
        List<Ranker> temp = rankerRepository.findHighestRankerByAccount(account);

        assert (temp.size() == 1);

        return (temp.size() == 0) ? null : temp.get(0);
    }

    public Ranker findHighestPointsByLadder(Ladder ladder) {
        List<Ranker> temp = rankerRepository.findHighestPointsByLadder(ladder);

        assert (temp.size() >= 1);

        return (temp.size() == 0) ? null : temp.get(0);
    }

    public List<Ranker> findAllRankerByLadder(Ladder ladder) {
        return rankerRepository.findAllRankerByLadder(ladder);
    }

    public List<Ranker> findAllRankerByLadderOrderedByPoints(Ladder ladder) {
        return rankerRepository.findAllRankerByLadderOrderedByPoints(ladder);
    }

    public List<Ranker> findAllRankerByLadderArea(Ranker ranker, Ladder ladder) {
        List<Ranker> results = findAllRankerByLadder(ladder);
        int size = results.size();
        int rank = ranker.getRank();
        int startRank = rank + 1 - (LADDER_AREA_SIZE / 2);
        int endRank = rank + (LADDER_AREA_SIZE / 2) - 1;

        if (endRank > size) {
            startRank = size + 1 - LADDER_AREA_SIZE;
            endRank = size;
        }

        if (startRank < 1) {
            startRank = 1;
            endRank = Math.min(LADDER_AREA_SIZE, size);
        }

        final int start = startRank;
        final int end = endRank;


        // Only Remove rankers that are not rank 1 or between start and end
        results.removeIf(r -> !(r.getRank() >= start && r.getRank() <= end));
        return results;
    }

    @Transactional
    public void updateAllRankerStats(List<Ranker> rankers) {
        rankers.forEach(ranker -> {
            rankerRepository.updateRankerStatsById(ranker.getId(), ranker.getRank(), ranker.getPoints(), ranker.getPower());
        });
    }

    @Transactional
    public boolean buyBias(Account account) {
        Ranker ranker = findHighestRankerByAccount(account);
        long cost = Math.round(Math.pow(ranker.getLadder().getNumber() + 1, ranker.getBias()));
        if (ranker.getPoints() >= cost) {
            ranker.setPoints(0L);
            //ranker.setPower(0L);
            ranker.setBias(ranker.getBias() + 1);
            rankerRepository.save(ranker);
            updateRankerRankByLadder(ranker.getLadder());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean buyMulti(Account account) {
        Ranker ranker = findHighestRankerByAccount(account);
        long cost = Math.round(Math.pow(ranker.getLadder().getNumber() + 1, ranker.getMultiplier()));
        if (ranker.getPower() >= cost) {
            ranker.setPoints(0L);
            ranker.setPower(0L);
            ranker.setBias(0);
            ranker.setMultiplier(ranker.getMultiplier() + 1);
            rankerRepository.save(ranker);
            updateRankerRankByLadder(ranker.getLadder());
            return true;
        }
        return false;
    }

    public void updateRankerRankByLadder(Ladder ladder) {
        List<Ranker> rankerList = findAllRankerByLadderOrderedByPoints(ladder);
        for (int i = 0; i < rankerList.size(); i++) {
            rankerList.get(i).setRank(i + 1);
        }
        updateAllRankerStats(rankerList);
    }


}