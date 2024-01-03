package com.soomo.dao

import com.soomo.domain.ServiceError
import zio.*
trait CommonDao[T]{
  def getAll(): IO[ServiceError, Seq[T]]
}