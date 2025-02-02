import Link from 'next/link';
import Image from 'next/image';

import BookmarkButton from './BookmarkButton';
import IconWithCounts from '../IconWithCounts';

import ReviewIcon from '/public/icons/review.svg';
import StarIcon from '/public/icons/star.svg';

import type { TPlace } from '@/types/response';

export default function Place({
  data,
  order,
  variant,
  isLoggedIn,
}: {
  data: TPlace;
  order: number;
  variant: 'primary' | 'secondary';
  isLoggedIn: boolean;
}) {
  const {
    id,
    place_name,
    thumbnail_image_url,
    review_count,
    rate,
    bookmarked,
  } = data;

  const postSize = variant === 'primary' ? 'w-[350px]' : 'w-[300px]';

  return (
    <article
      className={`${postSize} px-2 py-4 relative flex shrink-0 flex-col`}
    >
      <div className="relative w-full">
        <Link
          className="w-full relative aspect-square inline-block w-fit h-fit"
          href={`/place/${id}`}
        >
          <Image
            className="rounded-lg select-none"
            src={thumbnail_image_url}
            alt={`${place_name} 대표 이미지`}
            fill
            sizes="33vw"
          />
        </Link>
        <BookmarkButton
          className="absolute top-4 right-4"
          bookmarked={bookmarked}
          isLoggedIn={isLoggedIn}
        />
        {variant === 'primary' && (
          <Link tabIndex={-1} href={`place/${id}`}>
            <span className="absolute left-6 bottom-2 select-none text-white text-8xl font-semibold">
              {order + 1}
            </span>
          </Link>
        )}
      </div>
      <section className="flex flex-col gap-2 px-4">
        <Link
          className="w-fit text-gray-500 text-xl font-medium truncate select-none hover:underline"
          href={`place/${id}`}
        >
          {place_name}
        </Link>
        <div className="flex gap-3">
          <IconWithCounts
            icon={<ReviewIcon className="w-4 h-4" />}
            count={Number.parseInt(review_count, 10)}
          />
          <IconWithCounts
            icon={
              <StarIcon className="w-4 h-4 fill-[#FACC15] stroke-[#FACC15]" />
            }
            count={Number.parseFloat(rate)}
            rating
          />
        </div>
      </section>
    </article>
  );
}
