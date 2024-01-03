package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class TaxDeductionDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends TaxDeductionDao with CommonDao[TaxDeduction]:

  import ctx.*

  private inline def insertTaxDeduction(deduction: TaxDeduction): Quoted[Insert[TaxDeduction]] = quote {
    query[TaxDeduction]
      .insert(
        _.deductionType -> lift(deduction.deductionType),
        _.rate         -> lift(deduction.rate),
        _.payrollId    -> lift(deduction.payrollId)
      )
      .onConflictIgnore
  }

  def addAll(deductions: Seq[TaxDeduction]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(deductions.map(d => run(insertTaxDeduction(d))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[TaxDeduction]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[TaxDeduction]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, TaxDeduction] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[TaxDeduction]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[TaxDeduction].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object TaxDeductionDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, TaxDeductionDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield TaxDeductionDaoQuill(ctx)
    )
