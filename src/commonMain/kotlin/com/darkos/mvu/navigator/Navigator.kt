package com.darkos.mvu.navigator

interface Navigator{
    suspend fun navigate(navigation: Navigation): Boolean
}