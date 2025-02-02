package kr.co.yigil.member.domain;

import kr.co.yigil.file.AttachFile;
import kr.co.yigil.file.FileUploader;
import kr.co.yigil.follow.domain.FollowReader;
import kr.co.yigil.member.domain.MemberCommand.MemberUpdateRequest;
import kr.co.yigil.member.domain.MemberInfo.Main;
import kr.co.yigil.region.domain.MemberRegion;
import kr.co.yigil.region.domain.RegionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberReader memberReader;
    private final MemberStore memberStore;
    private final FollowReader followReader;
    private final FileUploader fileUploader;
    private final RegionReader regionReader;

    @Override
    @Transactional
    public Main retrieveMemberInfo(final Long memberId) {
        var member = memberReader.getMember(memberId);
        var followCount = followReader.getFollowCount(memberId);
        return new Main(member, followCount);
    }

    @Override
    @Transactional
    public void withdrawal(final Long memberId) {
        memberStore.deleteMember(memberId);
    }

    @Override
    @Transactional
    public void updateMemberInfo(final Long memberId, final MemberCommand.MemberUpdateRequest request) {

        var member = memberReader.getMember(memberId);
        AttachFile updatedProfile = getAttachFile(request);

        var memberRegions = regionReader.getRegions(request.getFavoriteRegionIds())
            .stream().map(region -> new MemberRegion(member, region))
            .toList();

        member.updateMemberInfo(request.getNickname(), request.getAges(), request.getGender(),
            updatedProfile , memberRegions);

     }

    private AttachFile getAttachFile(final MemberUpdateRequest request) {

        if(request.getProfileImageFile() != null) {
            return fileUploader.upload(request.getProfileImageFile());
        }
        return null;
    }
}
