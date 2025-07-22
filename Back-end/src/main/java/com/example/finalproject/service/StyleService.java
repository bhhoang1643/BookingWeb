package com.example.finalproject.service;

import com.example.finalproject.dto.StyleDTO;
import com.example.finalproject.entity.Agent;
import com.example.finalproject.entity.Style;
import com.example.finalproject.entity.StyleTag;
import com.example.finalproject.repository.AgentRepository;
import com.example.finalproject.repository.StyleRepository;
import com.example.finalproject.repository.StyleTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StyleService {

    private final StyleRepository styleRepository;
    private final AgentRepository agentRepository;
    private final StyleTagRepository styleTagRepository;

    public StyleService(StyleRepository styleRepository, AgentRepository agentRepository, StyleTagRepository styleTagRepository) {
        this.styleRepository = styleRepository;
        this.agentRepository = agentRepository;
        this.styleTagRepository = styleTagRepository;
    }


    public List<StyleDTO> getAllStyles() {
        return styleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public Optional<StyleDTO> getStyleById(Long id) {
        return styleRepository.findById(id).map(this::convertToDTO);
    }


    public List<StyleDTO> getStylesByAgentId(Integer agentId) {
        return styleRepository.findByAgentId(agentId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public Optional<StyleDTO> createStyle(StyleDTO styleDTO) {
        Optional<Agent> agentOpt = agentRepository.findById(styleDTO.getAgentId());
        Optional<StyleTag> tagOpt = styleTagRepository.findById(styleDTO.getStyleTagId());

        if (agentOpt.isEmpty() || tagOpt.isEmpty()) {
            return Optional.empty();
        }

        Style style = new Style(tagOpt.get(), agentOpt.get());
        Style savedStyle = styleRepository.save(style);
        return Optional.of(convertToDTO(savedStyle));
    }


    public Optional<StyleDTO> updateStyle(Long id, StyleDTO styleDTO) {
        Optional<Style> styleOpt = styleRepository.findById(id);
        Optional<Agent> agentOpt = agentRepository.findById(styleDTO.getAgentId());
        Optional<StyleTag> tagOpt = styleTagRepository.findById(styleDTO.getStyleTagId());

        if (styleOpt.isEmpty() || agentOpt.isEmpty() || tagOpt.isEmpty()) {
            return Optional.empty();
        }

        Style style = styleOpt.get();
        style.setAgent(agentOpt.get());
        style.setStyleTag(tagOpt.get());

        Style updatedStyle = styleRepository.save(style);
        return Optional.of(convertToDTO(updatedStyle));
    }


    public boolean deleteStyle(Long id) {
        if (!styleRepository.existsById(id)) {
            return false;
        }
        styleRepository.deleteById(id);
        return true;
    }


    private StyleDTO convertToDTO(Style style) {
        return new StyleDTO(
                style.getId(),
                style.getStyleTag().getId(),
                style.getStyleTag().getName(),
                style.getAgent().getId()
        );
    }
}
