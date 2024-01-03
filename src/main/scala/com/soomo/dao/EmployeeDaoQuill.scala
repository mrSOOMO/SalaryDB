package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class EmployeeDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends EmployeeDao with CommonDao[Employee]:

  import ctx.*

  private inline def insertEmployee(employee: Employee): Quoted[Insert[Employee]] = quote {
    query[Employee]
      .insert(
        _.firstName     -> lift(employee.firstName),
        _.lastName      -> lift(employee.lastName),
        _.departmentId  -> lift(employee.departmentId),
        _.positionId    -> lift(employee.positionId)
      )
      .onConflictIgnore
  }

  def addAll(employees: Seq[Employee]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(employees.map(emp => run(insertEmployee(emp))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Employee]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Employee]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Employee] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Employee]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Employee].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object EmployeeDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, EmployeeDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield EmployeeDaoQuill(ctx)
    )

