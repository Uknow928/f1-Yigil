import { getCoords } from './action';

import SearchIcon from '/public/icons/search.svg';
import XIcon from '/public/icons/x-mark.svg';

import type { Dispatch, SetStateAction } from 'react';

export default function SearchResult({
  searchResults,
  closeResults,
  setCurrentFoundPlace,
}: {
  searchResults: { name: string; roadAddress: string }[];
  closeResults: () => void;
  setCurrentFoundPlace: Dispatch<
    SetStateAction<
      | {
          name: string;
          roadAddress: string;
          coords: { lat: number; lng: number };
        }
      | undefined
    >
  >;
}) {
  async function handleClick(name: string, roadAddress: string) {
    const coords = await getCoords(roadAddress);

    if (!coords) {
      alert('좌표값을 얻어올 수 없습니다!');
      closeResults();
      return;
    }

    setCurrentFoundPlace({ name, roadAddress, coords });
    closeResults();
  }

  return (
    <section className="absolute flex flex-col w-full h-full">
      {searchResults.length !== 0 ? (
        <ul className="px-6 pt-2 flex flex-col grow gap-4">
          {searchResults.map(({ name, roadAddress }, index) => (
            <button
              className="p-2 text-xl text-gray-900 hover:text-gray-500 flex gap-4 items-center"
              onClick={() => handleClick(name, roadAddress)}
              key={`${name}-${index}`}
            >
              <SearchIcon />
              {name.length > 15 ? name.slice(0, 15) : name}
            </button>
          ))}
        </ul>
      ) : (
        <div className="h-full flex flex-col gap-16 justify-center items-center">
          <XIcon className="w-32 h-32" />
          <p>해당 검색어에 대한 장소를 찾을 수 없습니다!</p>
        </div>
      )}
    </section>
  );
}
