package turing.turing.domain.auth.apple;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplePublicKeys {

    private List<ApplePublicKey> keys;

    public ApplePublicKeys(List<ApplePublicKey> keys) {
        this.keys = List.copyOf(keys);
    }

    public ApplePublicKey getMatchingKeyBy(final String alg, final String kid) {
        return keys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("잘못된 토큰 형태입니다."));
    }
}
