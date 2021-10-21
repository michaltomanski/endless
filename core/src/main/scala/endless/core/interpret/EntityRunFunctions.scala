package endless.core.interpret

import endless.core.data.Folded
import cats.data.Chain
import cats.data.NonEmptyChain
import cats.syntax.either._
import cats.syntax.applicative._
import cats.conversions.all._
import cats.{Applicative, Monad}
import endless.core.data.EventsFolder

trait EntityRunFunctions {
  def pure[F[_]: Applicative, S, E, A](a: A)(events: Chain[E]): F[Folded[E, A]] =
    (events, a).asRight.pure

  def read[F[_]: Monad, S, E](
      folder: EventsFolder[S, E],
      events: Chain[E]
  ): F[Folded[E, Option[S]]] = folder.applyOnFoldable(events).map((events, _)).pure

  def write[F[_]: Applicative, S, E](
      newEvents: NonEmptyChain[E]
  )(existingEvents: Chain[E]): F[Folded[E, Unit]] =
    (existingEvents ++ newEvents.toChain, ()).asRight.pure
}
