package com.redis.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RedisController {

   private final RedisTemplate<String ,String > redisTemplate;

   @PostMapping("/redisTest")
   public ResponseEntity<?> addRedisKey() {
      ValueOperations<String, String> vop = redisTemplate.opsForValue();
      vop.set("yellow", "banana");
      vop.set("red", "apple");
      vop.set("green", "watermelon");
      return new ResponseEntity<>(HttpStatus.CREATED);
   }

   @GetMapping("/redisTest/{key}")
   public ResponseEntity<?> getRedisKey(@PathVariable String key) {
      ValueOperations<String, String> vop = redisTemplate.opsForValue();
      String value = vop.get(key);
      System.out.println("하이");
      return new ResponseEntity<>(value, HttpStatus.OK);
   }

}
