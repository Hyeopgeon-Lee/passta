package poly.persistance.redis.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import poly.dto.MelonDTO;
import poly.persistance.redis.IMelonMapper;
import poly.util.DateUtil;

@Component("MelonMapper")
public class MelonMapper implements IMelonMapper {

	@Autowired
	public RedisTemplate<String, Object> redisDB;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int insertSong(MelonDTO pDTO) throws Exception {

		log.info(this.getClass().getName() + ".insertSong Start!");

		int res = 0;

		// Redis에 저장될 키
		String key = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(MelonDTO.class));

		redisDB.opsForList().leftPush(key, pDTO);

		// 저장된 데이터는 하루동안 보관하기
		redisDB.expire(key, 1, TimeUnit.DAYS);

		res = 1;

		log.info(this.getClass().getName() + ".insertSong End!");

		return res;
	}

	@Override
	public List<Object> getSongList() throws Exception {

		log.info(this.getClass().getName() + ".getSongList Start!");

		// Redis에 저장된 키
		String key = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

		redisDB.setKeySerializer(new StringRedisSerializer());
		redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(MelonDTO.class));

		List<Object> rList = null;

		// 저장된 키가 존재한다면...
		if (redisDB.hasKey(key)) {
			rList = redisDB.opsForList().range(key, 0, -1);
		}

		log.info(this.getClass().getName() + ".getSongList End!");

		return rList;
	}

}
