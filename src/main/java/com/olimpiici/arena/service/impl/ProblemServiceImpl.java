package com.olimpiici.arena.service.impl;

import com.olimpiici.arena.domain.Problem;
import com.olimpiici.arena.repository.ProblemRepository;
import com.olimpiici.arena.service.ProblemService;
import com.olimpiici.arena.service.dto.ProblemDTO;
import com.olimpiici.arena.service.mapper.ProblemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Problem}.
 */
@Service
@Transactional
public class ProblemServiceImpl implements ProblemService {

    private final Logger log = LoggerFactory.getLogger(ProblemServiceImpl.class);

    private final ProblemRepository problemRepository;

    private final ProblemMapper problemMapper;

    public ProblemServiceImpl(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    @Override
    public ProblemDTO save(ProblemDTO problemDTO) {
        log.debug("Request to save Problem : {}", problemDTO);
        Problem problem = problemMapper.toEntity(problemDTO);
        problem = problemRepository.save(problem);
        return problemMapper.toDto(problem);
    }

    @Override
    public ProblemDTO update(ProblemDTO problemDTO) {
        log.debug("Request to update Problem : {}", problemDTO);
        Problem problem = problemMapper.toEntity(problemDTO);
        problem = problemRepository.save(problem);
        return problemMapper.toDto(problem);
    }

    @Override
    public Optional<ProblemDTO> partialUpdate(ProblemDTO problemDTO) {
        log.debug("Request to partially update Problem : {}", problemDTO);

        return problemRepository
            .findById(problemDTO.getId())
            .map(existingProblem -> {
                problemMapper.partialUpdate(existingProblem, problemDTO);

                return existingProblem;
            })
            .map(problemRepository::save)
            .map(problemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProblemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Problems");
        return problemRepository.findAll(pageable).map(problemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProblemDTO> findOne(Long id) {
        log.debug("Request to get Problem : {}", id);
        return problemRepository.findById(id).map(problemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Problem : {}", id);
        problemRepository.deleteById(id);
    }
}
