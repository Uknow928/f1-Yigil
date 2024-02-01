import Link from 'next/link';
import React, { ComponentType } from 'react';

interface TPopOverIcon {
  href: string;
  onClick?: () => void;
  label: string;
  closeModal: () => void;
  Icon: ComponentType<{ className?: string }>;
}

export default function PopOverIcon({
  href,
  onClick,
  label,
  closeModal,
  Icon,
}: TPopOverIcon) {
  return (
    <Link
      key={href}
      href={href}
      onClick={() => {
        onClick && onClick();
        closeModal();
      }}
      className="flex items-center gap-x-2 cursor-pointer"
    >
      <div>{label}</div>
      <Icon className="w-6 h-6" />
    </Link>
  );
}
