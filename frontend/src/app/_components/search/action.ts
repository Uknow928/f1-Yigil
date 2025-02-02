'use server';

import { dataWithAddressSchema, searchItemsSchema } from '@/types/response';

function searchUrl(keyword: string) {
  const endpoint = 'https://openapi.naver.com/v1/search/local.json';
  const queryString = `query=${keyword}&display=10&sort=random`;

  return `${endpoint}?${queryString}`;
}

function coordsUrl(address: string) {
  const endpoint =
    'https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode';
  const queryString = `query=${address}`;

  return `${endpoint}?${queryString}`;
}

interface TSearchResult {
  status: 'failed' | 'succeed';
  data: Array<{ name: string; roadAddress: string }>;
}

export async function searchAction(keyword: string): Promise<TSearchResult> {
  const response = await fetch(searchUrl(keyword), {
    method: 'GET',
    headers: {
      'X-Naver-Client-Id': process.env.NAVER_SEARCH_ID,
      'X-Naver-Client-Secret': process.env.NAVER_SEARCH_SECRET,
    },
  });

  const json = await response.json();
  const body = json.items;

  const items = searchItemsSchema.safeParse(body);

  if (!items.success) {
    console.log(items.error.errors);
    return { status: 'failed', data: [] };
  }

  const result = items.data.map(({ title, roadAddress }) => {
    const name = title.replace(/<b>|<\/b>/g, '');

    return { name, roadAddress };
  });

  return { status: 'succeed', data: result };
}

interface TCoordsResult {
  lat: number;
  lng: number;
}

export async function getCoords(
  roadAddress: string,
): Promise<{ lat: number; lng: number } | undefined> {
  const response = await fetch(coordsUrl(roadAddress), {
    method: 'GET',
    headers: {
      'X-NCP-APIGW-API-KEY-ID': process.env.NAVER_MAPS_CLIENT_ID,
      'X-NCP-APIGW-API-KEY': process.env.MAP_SECRET,
    },
  });

  const json = await response.json();

  const data = json.addresses;

  const coords = dataWithAddressSchema.safeParse(data);

  if (!coords.success) {
    return undefined;
  }

  const lat = Number.parseFloat(coords.data[0].y);
  const lng = Number.parseFloat(coords.data[0].x);

  return { lat, lng };
}
