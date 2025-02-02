export const dynamic = 'force-dynamic';

import React from 'react';

import KakaoBtn from '@/app/_components/ui/button/Kakao';
import GoogleLoginButton from '@/app/_components/ui/button/GoogleLoginButton';
import LoginLogo from '/public/logo/yigil_logo.svg';
import CloseButton from '@/app/_components/ui/button/CloseButton';

import { kakaoOAuthEndpoint } from '@/app/endpoints/api/auth/callback/kakao/constants';
import { googleOAuthEndPoint } from '@/app/endpoints/api/auth/callback/google/constants';

export default async function LoginPage() {
  const { KAKAO_ID, GOOGLE_CLIENT_ID } = process.env;

  const kakaoHref = await kakaoOAuthEndpoint(KAKAO_ID);
  const googleHref = await googleOAuthEndPoint(GOOGLE_CLIENT_ID);

  return (
    <div className="w-full h-full bg-main flex flex-col items-center">
      <CloseButton
        containerStyle="w-full flex justify-end h-1/6 mr-6 mt-5"
        style="cursor-pointer w-12 h-12 stroke-2 stroke-black hover:stroke-gray-700 active:stroke-gray-500"
      />
      <div className="h-2/6">
        <LoginLogo className="w-[290px] h-[96px]" />
      </div>
      <div className="w-full mx-7 h-2/6">
        <div className="w-full flex gap-x-4 px-8  relative ">
          <div className="border-t-[1px] border-white w-full h-1"></div>
          <div className="absolute top-[-12px] left-1/2 -translate-x-1/2 px-2 text-xl text-white bg-main">
            SNS로 간편로그인
          </div>
        </div>
        <div className="w-full mt-10 px-7 flex flex-col items-center justify-center gap-4">
          <KakaoBtn href={kakaoHref} />
          <GoogleLoginButton href={googleHref} />
        </div>
      </div>
    </div>
  );
}
