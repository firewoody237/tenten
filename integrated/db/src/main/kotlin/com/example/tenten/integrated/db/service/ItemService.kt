package com.example.tenten.integrated.db.service

import com.example.tenten.integrated.common.resultcode.ResultCode
import com.example.tenten.integrated.common.resultcode.ResultCodeException
import com.example.tenten.integrated.db.dto.CreateItemDTO
import com.example.tenten.integrated.db.dto.DeleteItemDTO
import com.example.tenten.integrated.db.dto.GetItemsDTO
import com.example.tenten.integrated.db.dto.UpdateItemDTO
import com.example.tenten.integrated.db.entity.ContentProvider
import com.example.tenten.integrated.db.entity.Item
import com.example.tenten.integrated.db.mapper.ItemMapper
import com.example.tenten.integrated.db.repository.ItemRepository
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val itemMapper: ItemMapper,
    private val itemRepository: ItemRepository,
    private val userApiService: UserApiService,
    private val contentProviderService: ContentProviderService
) {
    companion object {
        private val log = LogManager.getLogger()
    }

    fun getItem(id: Long?): Item {
        log.debug("call getItem : id = '$id'")

        if (id == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [id]이 존재하지 않습니다."
            )
        }

        try {
            return itemRepository.findById(id)
                .orElseThrow {
                    ResultCodeException(
                        resultCode = ResultCode.ERROR_ITEM_NOT_EXISTS,
                        loglevel = Level.WARN,
                        message = "getItem : id['${id}'] 상품이 존재하지 않습니다."
                    )
                }
        } catch(e: Exception) {
            log.error("getItem DB search failed. $id", e)
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "getItem 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun getItems(getItemsDTO: GetItemsDTO): MutableList<Item> {
        log.debug("call getItems : getItemsDTO = '$getItemsDTO'")

        when {
            (getItemsDTO.userId != null && getItemsDTO.contentProviderId != null) -> {
                return try {
                    itemRepository.findByCreatorIdAndContentProvider(
                        getItemsDTO.userId,
                        contentProviderService.getContentProvider(getItemsDTO.contentProviderId)
                    )
                } catch (e: Exception) {
                    throw ResultCodeException(
                        resultCode = ResultCode.ERROR_DB,
                        loglevel = Level.ERROR,
                        message = "getItem 호출 중 DB오류 발생 : ${e.message}"
                    )
                }
            }
            (getItemsDTO.userId != null) -> {
                return try {
                    itemRepository.findByCreatorId(
                        getItemsDTO.userId,
                    )
                } catch (e: Exception) {
                    throw ResultCodeException(
                        resultCode = ResultCode.ERROR_DB,
                        loglevel = Level.ERROR,
                        message = "getItem 호출 중 DB오류 발생 : ${e.message}"
                    )
                }
            }
            (getItemsDTO.contentProviderId != null) -> {
                return try {
                    itemRepository.findByContentProvider(
                        contentProviderService.getContentProvider(getItemsDTO.contentProviderId)
                    )
                } catch (e: Exception) {
                    throw ResultCodeException(
                        resultCode = ResultCode.ERROR_DB,
                        loglevel = Level.ERROR,
                        message = "getItem 호출 중 DB오류 발생 : ${e.message}"
                    )
                }
            }
            else -> {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                    loglevel = Level.WARN,
                    message = "파라미터가 존재하지 않습니다."
                )
            }
        }
    }

    fun getItemsByName(name: String?): MutableList<Item> {
        log.debug("call getItemsByName : name = '$name'")

        if (name.isNullOrEmpty()) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [id]이 존재하지 않습니다."
            )
        }

        return try {
            itemRepository.findByNameContains(name)
        } catch (e: Exception) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "deleteContentProvider 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun createItem(createItemDTO: CreateItemDTO): Item {
        log.debug("call createItem : createContentProviderDTO = '$createItemDTO'")

        if (createItemDTO.name.isNullOrEmpty()) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [name]이 존재하지 않습니다."
            )
        }

        if (createItemDTO.price == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [price]이 존재하지 않습니다."
            )
        }

        if (createItemDTO.creatorId == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [creatorId]이 존재하지 않습니다."
            )
        }

        if (createItemDTO.contentProvider == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [contentProvider]이 존재하지 않습니다."
            )
        }

        val creator = userApiService.getUserById(createItemDTO.creatorId)
        val contentProvider = contentProviderService.getContentProvider(createItemDTO.contentProvider.id)

        return when (isExistByNameAndContentProvider(createItemDTO.name, createItemDTO.contentProvider)) {
            true -> {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_ITEM_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                    message = "이미 존재하는 상품 입니다."
                )
            }
            else -> {
                try {
                    itemRepository.save(
                        Item(
                            name = createItemDTO.name,
                            price = createItemDTO.price,
                            creatorId = creator.id,
                            contentProvider = contentProvider,
                            itemCategory = createItemDTO.itemCategory
                        )
                    )
                } catch (e: Exception) {
                    throw ResultCodeException(
                        resultCode = ResultCode.ERROR_DB,
                        loglevel = Level.ERROR,
                        message = "createItem 호출 중 DB오류 발생 : ${e.message}"
                    )
                }
            }
        }
    }

    fun updateItem(updateItemDTO: UpdateItemDTO): Item {
        log.debug("call updateItem : updateItemDTO = '$updateItemDTO'")

        if (updateItemDTO.id == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [id]이 존재하지 않습니다."
            )
        }

        val item = getItem(updateItemDTO.id)

        val creator = userApiService.getUserById(updateItemDTO.creatorId)
        if (creator.id != item.creatorId) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_CREATOR_NOT_MATCH,
                loglevel = Level.INFO,
            )
        }

        if (updateItemDTO.name.isNullOrEmpty() && updateItemDTO.contentProvider != null) {
            if (isExistByNameAndContentProvider(item.name!!, updateItemDTO.contentProvider)) {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_ITEM_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                )
            }
        }

        if (updateItemDTO.name?.isNotEmpty() == true && updateItemDTO.contentProvider == null) {
            if (isExistByNameAndContentProvider(updateItemDTO.name, item.contentProvider!!)) {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_ITEM_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                )
            }
        }

        if (updateItemDTO.name?.isNotEmpty() == true && updateItemDTO.contentProvider != null) {
            if (isExistByNameAndContentProvider(updateItemDTO.name, updateItemDTO.contentProvider)) {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_ITEM_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                )
            }
        }

        var isChange = false

        if (updateItemDTO.name?.isNotEmpty() == true) {
            item.name = updateItemDTO.name
            isChange = true
        }

        if (updateItemDTO.price != null) {
            item.price = updateItemDTO.price
            isChange = true
        }

        if (updateItemDTO.contentProvider != null) {
            item.contentProvider = updateItemDTO.contentProvider
            isChange = true
        }

        if (updateItemDTO.itemCategory != null) {
            item.itemCategory = updateItemDTO.itemCategory
            isChange = true
        }

        return try {
            when (isChange) {
                true -> itemRepository.save(item)
                else -> throw ResultCodeException(
                    resultCode = ResultCode.ERROR_NOTHING_TO_MODIFY,
                    loglevel = Level.INFO
                )
            }
        } catch (e: Exception) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "updateItem 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun deleteItem(deleteItemDTO: DeleteItemDTO) {
        log.debug("call deleteItem : deleteItemDTO = '$deleteItemDTO'")

        if (deleteItemDTO.id == null) {
            throw ResultCodeException(
                ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [ID]가 존재하지 않습니다."
            )
        }

        if (deleteItemDTO.creatorId == null) {
            throw ResultCodeException(
                ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [creatorId]가 존재하지 않습니다."
            )
        }

        val creator = userApiService.getUserById(deleteItemDTO.creatorId)
        val item = getItem(deleteItemDTO.id)
        if (creator.id != item.creatorId) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_CREATOR_NOT_MATCH,
                loglevel = Level.INFO,
            )
        }

        try {
            itemRepository.deleteById(creator.id)
        } catch (e: Exception) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "deleteItem 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun isExistByNameAndContentProvider(name: String, contentProvider: ContentProvider): Boolean {
        log.debug("call isExistByNameAndContentProvider : name = '$name', contentProvider = '$contentProvider'")

        return try {
            itemRepository.existsByNameAndContentProvider(name, contentProvider)
        } catch (e: Exception) {
            log.error("isExistByNameAndContentProvider DB search failed. $name", e)
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "isExistByNameAndContentProvider 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }
}