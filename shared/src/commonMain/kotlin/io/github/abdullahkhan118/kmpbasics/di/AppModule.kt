package io.github.abdullahkhan118.kmpbasics.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("io.github.abdullahkhan118.kmpbasics") // scans this package + subpackages for annotated classes
class AppModule