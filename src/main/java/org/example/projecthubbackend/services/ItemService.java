package org.example.projecthubbackend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projecthubbackend.dtos.ItemDto;
import org.example.projecthubbackend.dtos.PositionDto;
import org.example.projecthubbackend.entities.Item;
import org.example.projecthubbackend.entities.Task;
import org.example.projecthubbackend.mappers.ItemMapper;
import org.example.projecthubbackend.repositories.ItemRepository;
import org.example.projecthubbackend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final TaskRepository taskRepository;

    public List<ItemDto> getAllItems(Long projectId, Long columnId, Long taskId) {
        List<Item> items = itemRepository.findAllByTaskColumnProjectIdAndTaskColumnIdAndTaskId(projectId, columnId, taskId);
        return items.stream().map(itemMapper::toDTO).collect(Collectors.toList());
    }

    public ItemDto findItem(Long projectId, Long columnId, Long taskId, Long id) {
        Item item = itemRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        return itemMapper.toDTO(item);
    }

    public ItemDto createItem(Long projectId, Long columnId, Long taskId, ItemDto itemDto) {
        Task task = taskRepository.findByProjectIdAndColumnIdAndId(projectId, columnId, taskId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        Integer maxPosition = itemRepository.findMaxPosition(taskId);
        int nextPosition = maxPosition == null ? 0 : maxPosition + 1;
        Item item = Item.builder().
                name(itemDto.getName()).
                isChecked(itemDto.getIsChecked()).
                position(nextPosition).
                task(task).
                build();
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDto updateItem(Long projectId, Long columnId, Long taskId, Long id, ItemDto itemDto) {
        Item item = itemRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Item Not Found"));
        if (itemDto.getName() != null)
            if (!itemDto.getName().equals(item.getName()))
                item.setName(itemDto.getName());
        if (itemDto.getIsChecked() != null)
            if (!itemDto.getIsChecked().equals(item.isChecked()))
                item.setChecked(itemDto.getIsChecked());
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public void removeItem(
            Long projectId,
            Long columnId,
            Long taskId,
            Long id) {
        itemRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(id);
    }

    public void changePosition(Long projectId, Long columnId, Long taskId, Long id, PositionDto dto) {
        //  lock task   //
        taskRepository.lockTask(projectId, columnId, taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        Item item = itemRepository.findByTaskColumnProjectIdAndTaskColumnIdAndTaskIdAndId(projectId, columnId, taskId, id).orElseThrow(() -> new EntityNotFoundException("Item Not Found"));

        int oldPosition = item.getPosition();
        int newPosition = dto.getNewPosition();

        if (newPosition < 0 || newPosition > itemRepository.findMaxPosition(taskId)) {
            throw new IllegalArgumentException("Invalid position: " + newPosition);
        }

        if (oldPosition == newPosition) return;

        if (oldPosition < newPosition) {
            itemRepository.shiftLeft(projectId, columnId, taskId, oldPosition, newPosition);
        } else {
            itemRepository.shiftRight(projectId, columnId, taskId, oldPosition, newPosition);
        }

        item.setPosition(newPosition);
        itemRepository.save(item);
    }
}
