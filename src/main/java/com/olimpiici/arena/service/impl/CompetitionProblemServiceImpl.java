package com.olimpiici.arena.service.impl;

import com.olimpiici.arena.domain.CompetitionProblem;
import com.olimpiici.arena.repository.CompetitionProblemRepository;
import com.olimpiici.arena.service.CompetitionProblemService;
import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import com.olimpiici.arena.service.mapper.CompetitionProblemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompetitionProblem}.
 */
@Service
@Transactional
public class CompetitionProblemServiceImpl implements CompetitionProblemService {

    private final Logger log = LoggerFactory.getLogger(CompetitionProblemServiceImpl.class);

    private final CompetitionProblemRepository competitionProblemRepository;

    private final CompetitionProblemMapper competitionProblemMapper;

    public CompetitionProblemServiceImpl(
        CompetitionProblemRepository competitionProblemRepository,
        CompetitionProblemMapper competitionProblemMapper
    ) {
        this.competitionProblemRepository = competitionProblemRepository;
        this.competitionProblemMapper = competitionProblemMapper;
    }

    @Override
    public CompetitionProblemDTO save(CompetitionProblemDTO competitionProblemDTO) {
        log.debug("Request to save CompetitionProblem : {}", competitionProblemDTO);
        CompetitionProblem competitionProblem = competitionProblemMapper.toEntity(competitionProblemDTO);
        competitionProblem = competitionProblemRepository.save(competitionProblem);
        return competitionProblemMapper.toDto(competitionProblem);
    }

    @Override
    public CompetitionProblemDTO update(CompetitionProblemDTO competitionProblemDTO) {
        log.debug("Request to update CompetitionProblem : {}", competitionProblemDTO);
        CompetitionProblem competitionProblem = competitionProblemMapper.toEntity(competitionProblemDTO);
        competitionProblem = competitionProblemRepository.save(competitionProblem);
        return competitionProblemMapper.toDto(competitionProblem);
    }

    @Override
    public Optional<CompetitionProblemDTO> partialUpdate(CompetitionProblemDTO competitionProblemDTO) {
        log.debug("Request to partially update CompetitionProblem : {}", competitionProblemDTO);

        return competitionProblemRepository
            .findById(competitionProblemDTO.getId())
            .map(existingCompetitionProblem -> {
                competitionProblemMapper.partialUpdate(existingCompetitionProblem, competitionProblemDTO);

                return existingCompetitionProblem;
            })
            .map(competitionProblemRepository::save)
            .map(competitionProblemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompetitionProblems");
        return competitionProblemRepository.findAll(pageable).map(competitionProblemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompetitionProblemDTO> findOne(Long id) {
        log.debug("Request to get CompetitionProblem : {}", id);
        return competitionProblemRepository.findById(id).map(competitionProblemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompetitionProblem : {}", id);
        competitionProblemRepository.deleteById(id);
    }
}
