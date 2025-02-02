import { NextRequest, NextResponse } from 'next/server';
import { authenticateUser } from './app/_components/mypage/hooks/authenticateUser';
import { myInfoSchema } from './types/response';

const restricted = ['/add', '/mypage'];

export default async function middleware(request: NextRequest) {
  const { ENVIRONMENT, PRODUCTION_FRONTEND_URL, DEV_FRONTEND_URL } =
    process.env;

  const baseUrl =
    ENVIRONMENT === 'production'
      ? PRODUCTION_FRONTEND_URL
      : ENVIRONMENT === 'dev'
      ? DEV_FRONTEND_URL
      : 'http://localhost:3000';

  const session = request.cookies.get('SESSION');

  if (restricted.some((pathname) => request.url.includes(pathname))) {
    const json = await authenticateUser();
    const member = myInfoSchema.safeParse(json);

    if (!member.success) {
      console.log(json);
      console.log(member.error);
      return NextResponse.redirect(`${baseUrl}/login`);
    }

    return NextResponse.next();
  }

  const response = NextResponse.next();

  if (!session) {
    const session = request.cookies.get('SESSION');

    if (!session) {
      return response;
    }

    response.cookies.set({
      name: session.name,
      value: session.value,
      httpOnly: true,
      secure: ENVIRONMENT === 'production',
      sameSite: 'strict',
    });

    return response;
  }

  return response;
}
