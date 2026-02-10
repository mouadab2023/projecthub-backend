package org.example.projecthubbackend.mappers;


import org.example.projecthubbackend.dtos.ItemDto;
import org.example.projecthubbackend.entities.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public ItemDto toDTO(Item item){
        return ItemDto.builder().
                id(item.getId()).
                name(item.getName()).
                isChecked(item.isChecked()).
                position(item.getPosition()).
                task(item.getTask()!=null?item.getTask().getId():null).
                build();
    }

    public Item toEntityBasics(ItemDto itemDto){
        return Item.builder().
                id(itemDto.getId()).
                name(itemDto.getName()).
                isChecked(itemDto.isChecked()).
                position(itemDto.getPosition()).
                build();
    }
}
