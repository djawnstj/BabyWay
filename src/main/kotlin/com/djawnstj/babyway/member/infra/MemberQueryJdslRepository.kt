package com.djawnstj.babyway.member.infra

import com.djawnstj.babyway.member.application.MemberQueryRepository
import com.djawnstj.babyway.member.domain.Member
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class MemberQueryJdslRepository(
    private val memberJpaRepository : MemberJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: RenderContext,
) : MemberQueryRepository {
    override fun existsByLoginIdAndNotDeleted(loginId: String?): Boolean {
        val query = jpql {
            select(
                entity(Member::class)
            ).from(
                entity(Member::class)
            ).where(
                path(Member::deletedAt).isNull()
                    .and(
                        path(Member::loginId).eq(loginId)
                    )
            )
        }

        return entityManager
            .createQuery(query, jpqlRenderContext)
            .apply { setMaxResults(1) }
            .resultList.isNotEmpty()
    }

    override fun findByIdAndNotDeleted(id: Long): Member? = memberJpaRepository.findAll {
        select(
            entity(Member::class)
        ).from(
            entity(Member::class)
        ).where(
            path(Member::deletedAt).isNull()
                .and(
                    path(Member::id).equal(id)
                )
        )
    }.firstOrNull()

    override fun findByLoginIdAndNotDeleted(loginId: String?): Member? = memberJpaRepository.findAll {
            select(
                entity(Member::class)
            ).from(
                entity(Member::class)
            ).where(
                path(Member::deletedAt).isNull()
                    .and(
                        path(Member::loginId).equal(loginId)
                    )
            )
        }.firstOrNull()
}
