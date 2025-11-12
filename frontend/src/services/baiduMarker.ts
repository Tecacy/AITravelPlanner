interface BaiduMarkerOptions {
  latitude: number;
  longitude: number;
  title: string;
  content: string;
  output?: 'html' | 'json' | 'xml';
  src?: string;
}

export const openBaiduMarker = (options: BaiduMarkerOptions) => {
  const {
    latitude,
    longitude,
    title,
    content,
    output = 'html',
    src = 'webapp.ai-travel-planner',
  } = options;

  const params = new URLSearchParams({
    location: `${latitude},${longitude}`,
    title,
    content,
    output,
    src,
  });

  const url = `http://api.map.baidu.com/marker?${params.toString()}`;
  window.open(url, '_blank', 'noopener,noreferrer');
};

