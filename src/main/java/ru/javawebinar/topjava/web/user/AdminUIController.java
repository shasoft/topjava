package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String name,
                       @RequestParam String email,
                       @RequestParam String password) {
        super.create(new User(null, name, email, password, Role.USER));
    }

    @PatchMapping(value = "/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enablePatch(@PathVariable int id, @RequestBody MultiValueMap<String, String> formParams) {
        boolean enabled = false;
        String enabledStr = formParams.getFirst("enabled");
        if (enabledStr != null) {
            enabled = Boolean.parseBoolean(enabledStr);
        }
        super.enable(id, enabled);
    }
}
