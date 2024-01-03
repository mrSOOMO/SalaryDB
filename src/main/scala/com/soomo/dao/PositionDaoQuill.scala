package com.soomo.dao

import com.soomo.domain.ServiceError
import com.soomo.domain.ServiceError.*
import com.soomo.domain.model.*
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class PositionDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends PositionDao with CommonDao[Position]:

  import ctx.*

  private inline def insertPosition(position: Position): Quoted[Insert[Position]] = quote {
    query[Position]
      .insert(
        _.title  -> lift(position.title),
        _.salary -> lift(position.salary)
      )
      .onConflictIgnore
  }

  def addAll(positions: Seq[Position]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(positions.map(pos => run(insertPosition(pos))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Position]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Position]))
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Position] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Position]).filter(_.id == lift(id))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Position].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object PositionDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, PositionDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield PositionDaoQuill(ctx)
    )
