package com.mleon.core.domain.exception

private const val ERROR_NO_ADDRESS = "No se encuentra la dirección del usuario"

class NoAddressException : Exception(ERROR_NO_ADDRESS)