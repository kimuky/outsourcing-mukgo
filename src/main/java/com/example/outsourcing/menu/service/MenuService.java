package com.example.outsourcing.menu.service;

import com.example.outsourcing.entity.Menu;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.menu.dto.MenuRequestDto;
import com.example.outsourcing.menu.dto.MenuResponseDto;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.status.MenuStatus;
import com.example.outsourcing.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public MenuResponseDto createMenu(Long storeId, Long userid, MenuRequestDto requestDto) {
        //가게 조회
        Store store = storeRepository.findByOrElseThrow(storeId);
        //사장님 권한 확인
        if(!store.getUser().getId().equals(userid)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        Menu menu = new Menu(
                store,
                requestDto.getMenuName(),
                requestDto.getPrice(),
                MenuStatus.SELLING
        );

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.toMene(savedMenu);
    }
}
