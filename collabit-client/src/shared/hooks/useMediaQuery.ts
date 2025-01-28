"use client";

import { useEffect, useState } from "react";

export function useMediaQuery(query: string, delay = 100) {
  const [matches, setMatches] = useState(false);

  useEffect(() => {
    let timeoutId: NodeJS.Timeout;
    const media = window.matchMedia(query);

    // 초기값 설정
    setMatches(media.matches);

    // 스로틀링이 적용된 리스너
    const listener = (e: MediaQueryListEvent) => {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => {
        setMatches(e.matches);
      }, delay);
    };

    media.addEventListener("change", listener);

    // cleanup
    return () => {
      clearTimeout(timeoutId);
      media.removeEventListener("change", listener);
    };
  }, [query, delay]);

  return matches;
}

export default useMediaQuery;
