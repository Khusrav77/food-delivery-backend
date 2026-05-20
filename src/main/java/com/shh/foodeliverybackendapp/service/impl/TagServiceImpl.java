package com.shh.foodeliverybackendapp.service.impl;

import com.shh.foodeliverybackendapp.dto.TagRequest;
import com.shh.foodeliverybackendapp.dto.TagResponse;
import com.shh.foodeliverybackendapp.entity.product.Tag;
import com.shh.foodeliverybackendapp.exception.EntityAlreadyExistsException;
import com.shh.foodeliverybackendapp.exception.EntityNotFoundException;
import com.shh.foodeliverybackendapp.mapper.TagMapper;
import com.shh.foodeliverybackendapp.repository.TagRepository;
import com.shh.foodeliverybackendapp.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepo;

    public TagServiceImpl(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public TagResponse create(TagRequest request) {
        if (tagRepo.existsByLabel(request.tagName())) {
            throw new EntityAlreadyExistsException(
                    "Tag with label '" + request.tagName() + "' already exists");
        }
        Tag tag = new Tag(request.tagName());
        return TagMapper.toResponse(tagRepo.save(tag));
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponse findById(UUID id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag", id));
        return TagMapper.toResponse(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        return tagRepo.findAll().stream()
                .map(TagMapper::toResponse)
                .toList();
    }

    @Override
    public TagResponse updateById(UUID id, TagRequest request) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag", id));
        tag.setLabel(request.tagName());
        return TagMapper.toResponse(tagRepo.save(tag));
    }

    @Override
    public void deleteById(UUID id) {
        if (!tagRepo.existsById(id)) {
            throw new EntityNotFoundException("Tag", id);
        }
        tagRepo.deleteById(id);
    }
}
