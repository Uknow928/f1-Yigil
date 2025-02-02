export function dataUrlToBlob(dataURI: string) {
  const byteString = atob(dataURI.split(',')[1]);

  // separate out the mime component
  const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

  // write the bytes of the string to an ArrayBuffer
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);
  for (var i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  return new Blob([ab], { type: mimeString });
}

export async function blobTodataUrl(blob: Blob) {
  const buffer = Buffer.from(await blob.arrayBuffer());

  return `data:${blob.type};base64,${buffer.toString('base64')}`;
}

export function getMIMETypeFromDataURI(dataURI: string) {
  const dataType = dataURI.split(',')[0];
  const types = dataType.split(':')[1];

  return types.split(';')[0];
}

export function coordsToGeoJSONPoint(coords: { lat: number; lng: number }) {
  return JSON.stringify({
    type: 'Point',
    coordinates: [coords.lng, coords.lat],
  });
}
