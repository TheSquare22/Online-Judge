package com.olimpiici.arena.service.impl;

import com.olimpiici.arena.domain.Competition;
import com.olimpiici.arena.repository.CompetitionRepository;
import com.olimpiici.arena.service.CompetitionService;
import com.olimpiici.arena.service.dto.CompetitionDTO;
import com.olimpiici.arena.service.mapper.CompetitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Competition}.
 */
@Service
@Transactional
public class CompetitionServiceImpl implements CompetitionService {

    private final Logger log = LoggerFactory.getLogger(CompetitionServiceImpl.class);

    private final CompetitionRepository competitionRepository;

    private final CompetitionMapper competitionMapper;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, CompetitionMapper competitionMapper) {
        this.competitionRepository = competitionRepository;
        this.competitionMapper = competitionMapper;
    }

    @Override
    public CompetitionDTO save(CompetitionDTO competitionDTO) {
        log.debug("Request to save Competition : {}", competitionDTO);
        Competition competition = competitionMapper.toEntity(competitionDTO);
        competition = competitionRepository.save(competition);
        return competitionMapper.toDto(competition);
    }

    @Override
    public CompetitionDTO update(CompetitionDTO competitionDTO) {
        log.debug("Request to update Competition : {}", competitionDTO);
        Competition competition = competitionMapper.toEntity(competitionDTO);
        competition = competitionRepository.save(competition);
        return competitionMapper.toDto(competition);
    }

    @Override
    public Optional<CompetitionDTO> partialUpdate(CompetitionDTO competitionDTO) {
        log.debug("Request to partially update Competition : {}", competitionDTO);

        return competitionRepository
            .findById(competitionDTO.getId())
            .map(existingCompetition -> {
                competitionMapper.partialUpdate(existingCompetition, competitionDTO);

                return existingCompetition;
            })
            .map(competitionRepository::save)
            .map(competitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Competitions");
        return competitionRepository.findAll(pageable).map(competitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompetitionDTO> findOne(Long id) {
        log.debug("Request to get Competition : {}", id);
        return competitionRepository.findById(id).map(competitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Competition : {}", id);
        competitionRepository.deleteById(id);
    }
}
