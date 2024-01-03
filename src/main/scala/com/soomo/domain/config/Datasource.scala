package com.soomo.domain.config


import domain.DbConfigPath
import io.getquill.jdbczio.Quill
import io.getquill.{PostgresJAsyncContext, SnakeCase}
import zio.{ZIO, ZLayer}

import javax.sql.DataSource

object Datasource:

  final private val quillPath: DbConfigPath  = "salary_app_db"
  final private val flywayPath: DbConfigPath = "flyway"

  val dataSourceLayer: ZLayer[Any, Throwable, DataSource] = Quill.DataSource.fromPrefix(flywayPath)
  val quillCtxLayer: ZLayer[Any, Throwable, PostgresJAsyncContext[SnakeCase]] = ZLayer(
    ZIO.attempt(new PostgresJAsyncContext[SnakeCase](SnakeCase, quillPath))
  )
