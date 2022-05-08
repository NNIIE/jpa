package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     *  준영속 엔티티를 수정하는 2가지 방법중 [변경감지(dirty checking) / 병합(merge)] 변경 감지 기능 사용
     *  디비에서 데이터를 찾아서 객체(findItem)에 저장한 후 값을 바꾸는 방법이다. (파라미터로 넘어온 Book객체가 준영속 상태의 엔티티)
     *  이미 트랜잭션이 일어난 경우 영속성 컨테스트가 더는 관리하지 않는 엔티티가 되기 때문에
     *  업데이트를 하려면 EntityManager를 통해 persist 같은 행위를 해야하지만
     *  이런식으로 디비에서 가져와 객체에 저장하는 순간 준영속 엔티티가 되기 때문에
     *  값을 바꿔주는 것만으로 JPA가 트랜잭션 시점에 데이터 변경을 감지하고 알아서 update를 날려준다.
     *  ** 병합을 사용할 시 모든 필드가 대체되서 필드가 null일 경우 null로 업데이트 되기 때문에 필드를 부분적으로 바꿔줄 수 있는 변경감지기능을 사용하자 **
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }
}
