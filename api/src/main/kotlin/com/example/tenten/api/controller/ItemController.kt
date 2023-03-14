package com.example.tenten.api.controller

import com.example.tenten.integrated.db.dto.CreateItemDTO
import com.example.tenten.integrated.db.dto.DeleteItemDTO
import com.example.tenten.integrated.db.dto.UpdateItemDTO
import com.example.tenten.integrated.db.service.ItemService
import com.example.tenten.integrated.webservice.api.ApiRequestMapping
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class ItemController(
    private val itemService: ItemService
) {
    companion object {
        private val log = LogManager.getLogger()
    }

    @ApiRequestMapping("/item/{id}", method = [RequestMethod.GET])
    fun getItem(@PathVariable id: Long): Any? {
        log.debug("getItem, id = '$id'")

        val item = itemService.getItem(id)

        return ItemVO(
            id = item.id,
            name = item.name!!,
            price = item.price!!,
            creatorId = item.creatorId!!,
            contentProvider = item.contentProvider!!,
            itemCategory = item.itemCategory!!
        )
    }

    @ApiRequestMapping("/item", method = [RequestMethod.POST])
    fun createItem(@RequestBody createItemDTO: CreateItemDTO): Any? {
        log.debug("createItem, createItemDTO = '$createItemDTO'")

        val item = itemService.createItem(createItemDTO)

        return ItemVO(
            id = item.id,
            name = item.name!!,
            price = item.price!!,
            creatorId = item.creatorId!!,
            contentProvider = item.contentProvider!!,
            itemCategory = item.itemCategory!!
        )
    }

    @ApiRequestMapping("/item", method = [RequestMethod.PATCH])
    fun updateItem(@RequestBody updateItemDTO: UpdateItemDTO): Any? {
        log.debug("updateItem, updateItemDTO = '$updateItemDTO'")

        val item = itemService.updateItem(updateItemDTO)

        return ItemVO(
            id = item.id,
            name = item.name!!,
            price = item.price!!,
            creatorId = item.creatorId!!,
            contentProvider = item.contentProvider!!,
            itemCategory = item.itemCategory!!
        )
    }

    @ApiRequestMapping("/item", method = [RequestMethod.DELETE])
    fun deleteItem(@RequestBody deleteItemDTO: DeleteItemDTO) {
        log.debug("deleteItem, deleteItemDTO = '$deleteItemDTO'")

        itemService.deleteItem(deleteItemDTO)
    }
}