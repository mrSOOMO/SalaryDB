package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class DepartmentDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends DepartmentDao with CommonDao[Department]:

  import ctx.*

  private inline def insertDepartment(department: Department): Quoted[Insert[Department]] = quote {
    query[Department]
      .insert(
        _.name    -> lift(department.name),
        _.manager -> lift(department.manager)
      )
      .onConflictIgnore
  }

  def addAll(departments: Seq[Department]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(departments.map(dept => run(insertDepartment(dept))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Department]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Department]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Department] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Department]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Department].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)


/**
 * Компаньйон-об'єкт для `DepartmentDaoQuill`.
 *
 * Цей об'єкт надає ZLayer, який використовується для створення екземпляра `DepartmentDaoQuill`.
 * Він залежить від `PostgresJAsyncContext[SnakeCase]` для доступу до бази даних.
 *
 * Використання:
 * {{{
 *   val departmentDaoLayer = DepartmentDaoQuill.live
 * }}}
 */
object DepartmentDaoQuill:

  /**
   * Надає ZLayer для створення `DepartmentDaoQuill`.
   *
   * @return ZLayer, який потребує `PostgresJAsyncContext[SnakeCase]` і створює `DepartmentDaoQuill`.
   */
  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, DepartmentDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield DepartmentDaoQuill(ctx)
    )