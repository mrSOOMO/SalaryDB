package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class TimesheetDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends TimesheetDao with CommonDao[Timesheet]:

  import ctx.*

  private inline def insertTimesheet(timesheet: Timesheet): Quoted[Insert[Timesheet]] = quote {
    query[Timesheet]
      .insert(
        _.date        -> lift(timesheet.date),
        _.hours       -> lift(timesheet.hours),
        _.employeeId  -> lift(timesheet.employeeId)
      )
      .onConflictIgnore
  }

  def addAll(timesheets: Seq[Timesheet]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(timesheets.map(ts => run(insertTimesheet(ts))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Timesheet]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Timesheet]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Timesheet] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Timesheet]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Timesheet].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object TimesheetDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, TimesheetDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield TimesheetDaoQuill(ctx)
    )

