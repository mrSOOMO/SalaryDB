package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class PayrollDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends PayrollDao with CommonDao[Payroll]:

  import ctx.*

  private inline def insertPayroll(payroll: Payroll): Quoted[Insert[Payroll]] = quote {
    query[Payroll]
      .insert(
        _.amount       -> lift(payroll.amount),
        _.paymentDate  -> lift(payroll.paymentDate),
        _.employeeId   -> lift(payroll.employeeId)
      )
      .onConflictIgnore
  }

  def addAll(payrolls: Seq[Payroll]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(payrolls.map(pr => run(insertPayroll(pr))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Payroll]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Payroll]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Payroll] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Payroll]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Payroll].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object PayrollDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, PayrollDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield PayrollDaoQuill(ctx)
    )
