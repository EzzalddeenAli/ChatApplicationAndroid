package com.coders.chatapplication.data.repository.auth

import com.coders.chatapplication.data.net.interceptors.AuthInterceptor
import com.coders.chatapplication.data.repository.auth.datasource.RemoteDataSource
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.domain.repository.AuthRepository
import com.coders.stompclient.StompClient

class AuthRepositoryImpl(
	private val remoteDataSource: RemoteDataSource,
	private val authInterceptor: AuthInterceptor,
	private val stompClient: StompClient,
	private val sharedPrefs: SharedPrefs
) : AuthRepository {
	override suspend fun login(email: String, password: String): UserModel {
		val response = remoteDataSource.login(email, password)
		val authorization = response.headers()["Authorization"]
		authInterceptor.authToken = authorization
		if (response.code() < 400) {
			stompClient.connect()
		}
		sharedPrefs.authToken = authorization ?: ""
		sharedPrefs.userId = response.body()?.id ?: -1
		sharedPrefs.userEmail = response.body()?.email ?: ""
		return response.body()?.asDomain()!!
	}

	override suspend fun getPrincipal(): UserModel {
		return remoteDataSource.getPrincipal().asDomain()
	}
}