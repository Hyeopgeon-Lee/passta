package poly.service;

import java.util.List;
import java.util.Map;

public interface IMelonService {

	/**
	 * 멜론 노래 리스트 저장하기
	 */
	public int collectMelonSong() throws Exception;

	/**
	 * 오늘 수집된 멜론 노래리스트 가져오기
	 */
	public List<Map<String, String>> getSongList() throws Exception;

}
