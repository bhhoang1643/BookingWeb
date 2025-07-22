package com.example.finalproject.service;

import com.example.finalproject.dto.StyleTagDTO;
import com.example.finalproject.entity.StyleTag;
import com.example.finalproject.repository.StyleTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StyleTagService {

    private final StyleTagRepository styleTagRepository;

    public StyleTagService(StyleTagRepository styleTagRepository) {
        this.styleTagRepository = styleTagRepository;
    }

    public List<StyleTagDTO> getAllTags() {
        return styleTagRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StyleTagDTO getTagById(Long id) {
        StyleTag tag = styleTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ StyleTag not found"));
        return toDTO(tag);
    }

    public StyleTagDTO createTag(StyleTagDTO dto) {
        if (styleTagRepository.existsByName(dto.getName())) {
            throw new RuntimeException("❌ StyleTag already exists");
        }
        StyleTag saved = styleTagRepository.save(new StyleTag(dto.getName()));
        return toDTO(saved);
    }

    public StyleTagDTO updateTag(Long id, StyleTagDTO dto) {
        StyleTag tag = styleTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ StyleTag not found"));

        tag.setName(dto.getName());
        return toDTO(styleTagRepository.save(tag));
    }

    public void deleteTag(Long id) {
        if (!styleTagRepository.existsById(id)) {
            throw new RuntimeException("❌ StyleTag not found");
        }
        styleTagRepository.deleteById(id);
    }

    private StyleTagDTO toDTO(StyleTag tag) {
        return new StyleTagDTO(tag.getId(), tag.getName());
    }
}
