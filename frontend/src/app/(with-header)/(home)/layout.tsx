import HomeNavigation from './HomeNavigation';

import type { ReactNode } from 'react';

export default function HomeLayout({ children }: { children: ReactNode }) {
  return (
    <section className="flex flex-col grow">
      <HomeNavigation />
      {children}
    </section>
  );
}
