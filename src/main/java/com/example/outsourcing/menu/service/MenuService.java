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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * 메뉴 생성
     * @param storeId
     * @param userid
     * @param requestDto
     * @return
     */
    public MenuResponseDto createMenu(Long storeId, Long userid, MenuRequestDto requestDto) {
        //가게 조회
        Store store = storeRepository.findByStoreOrElseThrow(storeId);
        //사장님 권한 확인
        if(!store.getUser().getId().equals(userid)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        Menu menu = new Menu(
                store,
                requestDto.getMenuName(),
                requestDto.getPrice(),
                MenuStatus.SELLING
        );

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.fromMenu(savedMenu);
    }


    /**
     * 메뉴 수정
     * @param storeId
     * @param menuId
     * @param userId
     * @param menuName
     * @param price
     * @return
     */
    public MenuResponseDto updateMenu(Long storeId, Long menuId ,Long userId, String menuName, Integer price) {

        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        //사장님 권한 확인
        if(!findStore.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        Menu findMenu = menuRepository.findByMenuOrElseThrow(menuId);

        if(menuName == null || price == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        findMenu.updateMenu(menuName, price);

        Menu updatedMenu = menuRepository.save(findMenu);

        return MenuResponseDto.fromMenu(updatedMenu);
    }


    /**
     * 메뉴 삭제
     * @param storeId
     * @param menuId
     * @param userId
     */
    @Transactional
    public void deleteMenu(Long storeId, Long menuId, Long userId) {
        //가게 조회
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        //사용자 검증
        if(!findStore.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        //메뉴 조회
        Menu findMenu = menuRepository.findByMenuOrElseThrow(menuId);

        //상태 토글
        if(findMenu.getStatus() == MenuStatus.SELLING) {
            findMenu.setStatus(MenuStatus.DELETED);
        } else {
            findMenu.setStatus(MenuStatus.SELLING);
        }
    }
}
