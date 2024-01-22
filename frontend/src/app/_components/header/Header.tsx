'use client';
import { signOut, useSession } from 'next-auth/react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import React, { useState } from 'react';
import RoundProfile from '../ui/profile/RoundProfile';
import HeaderLogo from '/public/logo/yigil_logo.svg';
import MapPin from '/public/icons/map-pin.svg';
import Bell from '/public/icons/bell.svg';

export default function Header() {
  const { data, status } = useSession();
  const router = useRouter();
  const [isOpenedModal, setIsOpenedModal] = useState(false);

  return (
    <div className="w-[430px] h-[80px] flex justify-between items-center fixed top-0 bg-main">
      <div className="ml-4 cursor-pointer" onClick={() => router.push('/')}>
        <HeaderLogo />
      </div>
      {status === 'loading' ? (
        <></>
      ) : status === 'authenticated' ? (
        <>
          <span onClick={() => setIsOpenedModal(true)}>
            <RoundProfile
              img={data.user?.image as string}
              size={40}
              style="cursor-pointer"
              height="h-[40px]"
            />
          </span>
          {isOpenedModal && (
            <>
              <div
                className="fixed inset-0"
                onClick={() => setIsOpenedModal(false)}
              ></div>
              <div className="absolute bottom-[-90px] right-4 w-[134px] h-[104px] bg-[#F3F4F6] rounded-md flex flex-col items-center justify-center gap-5">
                <Link
                  href="/mypage"
                  className="flex items-center"
                  onClick={() => setIsOpenedModal(false)}
                >
                  <div>마이페이지</div>
                  <MapPin />
                </Link>
                <Link
                  href="/"
                  className="flex items-center"
                  onClick={() => signOut()}
                >
                  <div>로그아웃</div>
                  <Bell />
                </Link>
              </div>
            </>
          )}
        </>
      ) : (
        <Link
          href="/login"
          className="flex justify-center items-center w-[68px] h-10 mr-4 border-[1px] border-white border-solid rounded-md text-white text-xl no-underline"
        >
          로그인
        </Link>
      )}
      {/**user 있으면 프로필 없으면 로그인 */}
    </div>
  );
}
