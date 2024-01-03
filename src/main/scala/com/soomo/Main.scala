package com.soomo

import com.soomo.dao.*
import com.soomo.domain.config.Datasource.{dataSourceLayer, quillCtxLayer}
import com.soomo.flyway.{Flyway, FlywayProvider}
import com.soomo.{Menu, *}
import com.typesafe.config.{Config, ConfigFactory}
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.middleware.RequestHandlerMiddlewares
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}

import java.io.IOException
import java.net.InetSocketAddress

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(
      TypesafeConfigProvider
        .fromResourcePath()
    )

  private val app = for {
    flyway: Flyway <- FlywayProvider.flyway
    _              <- flyway.migrate
    hostDB = sys.env.getOrElse("POSTGRES_HOST", ConfigFactory.load().getString("salary_app_db.db.host"))
    portDB = sys.env.getOrElse("POSTGRES_PORT", ConfigFactory.load().getString("salary_app_db.db.port"))
    res <- EmployeeDao
      .getAll()
      .fold(error => s"DB access ERROR using $hostDB:$portDB: $error",
        employees => s"DB Connected using $hostDB:$portDB"
      )
    _ <- printWithFrame(res)
    _ <- printWithFrame(s"App Started")
    menu <- ZIO.service[Menu]
    _    <- menu.runMenu
  } yield ()

  override val run: ZIO[Any, Throwable, Unit] = app.provide(
    dataSourceLayer,
    quillCtxLayer,
    Menu.live,
    DepartmentDaoQuill.live,
    PositionDaoQuill.live,
    EmployeeDaoQuill.live,
    TimesheetDaoQuill.live,
    PayrollDaoQuill.live,
    TaxDeductionDaoQuill.live,
    FlywayProvider.live
  )
}
