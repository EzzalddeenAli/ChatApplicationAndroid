package com.coders.chatapplication.domain.usecase.chat

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import com.coders.chatapplication.commons.domain.usecase.UseCase
import com.coders.chatapplication.domain.repository.ChatManager

class SubscribeToRoomUseCase(
	private val chatManager: ChatManager
) : UseCase<Long, Unit>() {

	override suspend fun execute(params: Long): Either<Failure, Unit> {
		return try {
			chatManager.subscribeToRoom(params)
			Either.Right(Unit)
		} catch (e: Exception) {
			Either.Left(Failure.FeatureFailure(e))
		}
	}
}