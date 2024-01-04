package kr.co.yigil.travel.domain;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import kr.co.yigil.file.FileType;
import kr.co.yigil.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@DiscriminatorValue("SPOT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spot extends Travel{
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    private boolean isInCourse;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    public Spot(final Long id) {
        super.setId(id);
    }

    public Spot(final Long id,final Point location, final boolean isInCourse, final String title, final String description, final String fileUrl) {
        super.setId(id);
        this.location = location;
        this.isInCourse = isInCourse;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
    }
    public Spot(final Point location, final boolean isInCourse, final String title, final String description, final String fileUrl) {
        this.location = location;
        this.isInCourse = isInCourse;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
    }

    public Spot(final Point location, final String title, final String description, final String fileUrl) {
        this.location = location;
        this.isInCourse = false;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
    }

//    public void setId(Long id){
//        super.setId(id);
//    }
}
