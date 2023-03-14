package com.example.tenten.integrated.db.service

import com.example.tenten.integrated.common.resultcode.ResultCode
import com.example.tenten.integrated.common.resultcode.ResultCodeException
import com.example.tenten.integrated.db.dto.CreateContentProviderDTO
import com.example.tenten.integrated.db.dto.DeleteContentProviderDTO
import com.example.tenten.integrated.db.dto.UpdateContentProviderDTO
import com.example.tenten.integrated.db.entity.ContentProvider
import com.example.tenten.integrated.db.mapper.ContentProviderMapper
import com.example.tenten.integrated.db.repository.ContentProviderRepository
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class ContentProviderService(
    private val contentProviderMapper: ContentProviderMapper,
    private val contentProviderRepository: ContentProviderRepository
) {
    companion object {
        private val log = LogManager.getLogger()
    }

    fun getContentProvider(id: Long?): ContentProvider {
        log.debug("call getContentProvider : id = '$id'")

        if (id == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [id]이 존재하지 않습니다."
            )
        }

        try {
            return contentProviderRepository.findById(id)
                .orElseThrow {
                    ResultCodeException(
                        resultCode = ResultCode.ERROR_CONTENT_PROVIDER_NOT_EXISTS,
                        loglevel = Level.WARN,
                        message = "getContentProvider : id['${id}'] 컨텐츠 제공자가 존재하지 않습니다."
                    )
                }
        } catch(e: Exception) {
            log.error("getContentProvider DB search failed. ${id}", e)
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "getContentProvider 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun createContentProvider(createContentProviderDTO: CreateContentProviderDTO): ContentProvider {
        log.debug("call createContentProvider : createContentProviderDTO = '$createContentProviderDTO'")

        if (createContentProviderDTO.name.isNullOrEmpty()) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [name]이 존재하지 않습니다."
            )
        }

        if (createContentProviderDTO.service == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [service]이 존재하지 않습니다."
            )
        }

        return when(isExistByName(createContentProviderDTO.name)) {
            true -> {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_CONTENT_PROVIDER_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                    message = "'${createContentProviderDTO.name}'은 이미 존재하는 [Name]입니다."
                )
            }
            false -> {
                try {
                    contentProviderRepository.save(
                        ContentProvider(
                            name = createContentProviderDTO.name,
                            service = createContentProviderDTO.service
                        )
                    )
                } catch (e: Exception) {
                    log.error("createContentProvider failed. $createContentProviderDTO", e)
                    throw ResultCodeException(
                        resultCode = ResultCode.ERROR_DB,
                        loglevel = Level.ERROR,
                        message = "createContentProvider 호출 중 DB오류 발생 : ${e.message}"
                    )
                }
            }
        }
    }

    fun isExistByName(name: String): Boolean {
        log.debug("call isExistByName : name = '$name'")

        return try {
            contentProviderRepository.existsByName(name)
        } catch (e: Exception) {
            log.error("isExistByName DB search failed. $name", e)
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "isExistByName 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun isExistById(id: Long): Boolean {
        log.debug("call isExistById : id = '$id'")

        return try {
            contentProviderRepository.existsById(id)
        } catch (e: Exception) {
            log.error("isExistByName DB search failed. $id", e)
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "isExistByName 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun updateContentProvider(updateContentProviderDTO: UpdateContentProviderDTO): ContentProvider {
        log.debug("call updateContentProvider : updateContentProviderDTO = '$updateContentProviderDTO'")

        if (updateContentProviderDTO.id == null) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [ID]가 존재하지 않습니다."
            )
        }

        if (updateContentProviderDTO.name?.isNotEmpty() == true) {
            if (isExistByName(updateContentProviderDTO.name)) {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_USER_ALREADY_EXISTS,
                    loglevel = Level.INFO,
                    message = "'${updateContentProviderDTO.name}'은 이미 존재하는 [Name]입니다."
                )
            }
        }

        var isChange = false
        val contentProvider = getContentProvider(updateContentProviderDTO.id)

        if (updateContentProviderDTO.name?.isNotEmpty() == true) {
            contentProvider.name = updateContentProviderDTO.name
            isChange = true
        }

        if (updateContentProviderDTO.service != null) {
            contentProvider.service = updateContentProviderDTO.service
            isChange = true
        }

        return try {
            when (isChange) {
                true -> contentProviderRepository.save(contentProvider)
                else -> throw ResultCodeException(
                    resultCode = ResultCode.ERROR_NOTHING_TO_MODIFY,
                    loglevel = Level.INFO
                )
            }
        } catch (e: Exception) {
            throw ResultCodeException(
                resultCode = ResultCode.ERROR_DB,
                loglevel = Level.ERROR,
                message = "updateContentProvider 호출 중 DB오류 발생 : ${e.message}"
            )
        }
    }

    fun deleteContentProvider(deleteContentProviderDTO: DeleteContentProviderDTO) {
        log.debug("call deleteContentProvider : deleteContentProviderDTO = '$deleteContentProviderDTO'")

        if (deleteContentProviderDTO.id == null) {
            throw ResultCodeException(
                ResultCode.ERROR_PARAMETER_NOT_EXISTS,
                loglevel = Level.WARN,
                message = "파라미터에 [ID]가 존재하지 않습니다."
            )
        }

        if (isExistById(deleteContentProviderDTO.id)) {
            try {
                contentProviderRepository.deleteById(deleteContentProviderDTO.id)
            } catch (e: Exception) {
                throw ResultCodeException(
                    resultCode = ResultCode.ERROR_DB,
                    loglevel = Level.ERROR,
                    message = "deleteContentProvider 호출 중 DB오류 발생 : ${e.message}"
                )
            }
        }
    }
}