package com.caseyjbrooks.di

import org.koin.core.module.Module

public fun KoinModule.getModulesForVariant(variant: Variant): List<Module> {
    val koinModule = this
    return buildList {
        val listBuilder = this
        // always include the mainModule
        listBuilder += koinModule.mainModule()

        // include the module for the environment
        listBuilder += when (variant.environment) {
            Variant.Environment.Test -> {
                koinModule.testModule()
            }

            Variant.Environment.Local -> {
                koinModule.localModule()
            }

            Variant.Environment.Qa -> {
                koinModule.qaModule()
            }

            Variant.Environment.Production -> {
                koinModule.productionModule()
            }
        }

        // include the module for the build type
        listBuilder += when (variant.buildType) {
            Variant.BuildType.Debug -> {
                koinModule.debugModule()
            }

            Variant.BuildType.Release -> {
                koinModule.releaseModule()
            }
        }

        // if a platform-specific module is provided, recursively add its modules as well
        koinModule.platformKoinModule()?.getModulesForVariant(variant)?.let { platformModules ->
            listBuilder += platformModules
        }

        // for all related modules, recursively add those modules
        koinModule.relatedModules().forEach { relatedModule ->
            listBuilder += relatedModule.getModulesForVariant(variant)
        }
    }
}
