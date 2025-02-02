package kr.co.yigil.travel.infrastructure;

import java.util.Optional;
import kr.co.yigil.travel.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    Optional<Travel> findByIdAndMemberId(Long travelId, Long memberId);
}