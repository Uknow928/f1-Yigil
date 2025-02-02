import { logout } from '../../api/action';

import MapPin from '/public/icons/map-pin.svg';
import Bell from '/public/icons/bell.svg';
import LocationIcon from '/public/icons/location.svg';
import CalendarIcon from '/public/icons/calendar.svg';

import type { TPopOverData } from './types';

export const headerPopOverData: TPopOverData[] = [
  {
    href: '/mypage/my/travel/spot',
    label: '마이페이지',
    icon: <MapPin className="w-6 h-6 stroke-black" />,
  },
  {
    href: '/',
    label: '로그아웃',
    icon: <Bell className="w-6 h-6" />,
    onClick: async () => {
      await logout();
      location.reload();
    },
  },
];

export const homePopOverData: TPopOverData[] = [
  {
    href: '/add/spot',
    label: '장소 추가하기',
    icon: <LocationIcon className="w-6 h-6" />,
  },
  {
    href: '/add/course',
    label: '일정 추가하기',
    icon: <CalendarIcon className="w-6 h-6" />,
  },
];
