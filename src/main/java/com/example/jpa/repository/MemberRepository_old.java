package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository_old {
//    @PersistenceContext
//    private EntityManager em;
    private final EntityManager em; //spring data jpa 덕분에 @PersistenceContext를 쓰지 않고 생성자 injection 방식으로도 가능
    public void save (Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
