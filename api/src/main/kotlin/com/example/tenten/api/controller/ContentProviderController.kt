package com.example.tenten.api.controller

import com.example.tenten.integrated.db.dto.CreateContentProviderDTO
import com.example.tenten.integrated.db.dto.DeleteContentProviderDTO
import com.example.tenten.integrated.db.dto.UpdateContentProviderDTO
import com.example.tenten.integrated.db.service.ContentProviderService
import com.example.tenten.integrated.webservice.api.ApiRequestMapping
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class ContentProviderController(
    private val contentProviderService: ContentProviderService
) {
    companion object {
        private val log = LogManager.getLogger()
    }

    @ApiRequestMapping("/cp/{id}", method = [RequestMethod.GET])
    fun getContentProvider(@PathVariable id: Long): Any? {
        log.debug("getContentProvider, id = '$id'")

        val cp = contentProviderService.getContentProvider(id)

        return ContentProviderVO(
            id = cp.id,
            name = cp.name!!,
            service = cp.service
        )
    }

    @ApiRequestMapping("/cp", method = [RequestMethod.POST])
    fun createContentProvider(@RequestBody createContentProviderDTO: CreateContentProviderDTO): Any? {
        log.debug("createContentProvider, createContentProviderDTO = '$createContentProviderDTO'")

        val cp = contentProviderService.createContentProvider(createContentProviderDTO)

        return ContentProviderVO(
            id = cp.id,
            name = cp.name!!,
            service = cp.service
        )
    }

    @ApiRequestMapping("/cp", method = [RequestMethod.PUT])
    fun updateContentProvider(@RequestBody updateContentProviderDTO: UpdateContentProviderDTO): Any? {
        log.debug("updateContentProvider, updateContentProviderDTO = '$updateContentProviderDTO'")

        val cp = contentProviderService.updateContentProvider(updateContentProviderDTO)

        return ContentProviderVO(
            id = cp.id,
            name = cp.name!!,
            service = cp.service
        )
    }

    @ApiRequestMapping("/cp", method = [RequestMethod.DELETE])
    fun deleteContentProvider(@RequestBody deleteContentProviderDTO: DeleteContentProviderDTO) {
        log.debug("deleteContentProvider, deleteContentProviderDTO = '$deleteContentProviderDTO'")

        contentProviderService.deleteContentProvider(deleteContentProviderDTO)
    }
}