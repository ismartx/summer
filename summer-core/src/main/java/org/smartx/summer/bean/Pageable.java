package org.smartx.summer.bean;

import java.util.List;
import java.util.Locale;

/**
 * <p> 分页 </p>
 *
 * <b>Creation Time:</b> 2016年10月31日
 *
 * @author binglin
 * @since summer 0.1
 */
public class Pageable {

    private Integer page = 0;

    private Integer size = 10;

    private List<Sort> sorts;

    public Pageable() {
    }

    public Pageable(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Pageable(Integer page, Integer size, List<Sort> sorts) {
        this.page = page;
        this.size = size;
        this.sorts = sorts;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getOffset() {
        return page * size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public static class Sort {

        private String name;

        private Direction direction;

        public Sort(String name, Direction direction) {
            this.name = name;
            this.direction = direction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }
    }

    public enum Direction {

        // 顺序
        ASC,
        // 倒序
        DESC;

        /**
         * Returns the {@link Direction} enum for the given {@link String} value.
         *
         * @throws IllegalArgumentException in case the given value cannot be parsed into an enum
         *                                  value.
         */
        public static Direction fromString(String value) {

            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }

        /**
         * Returns the {@link Direction} enum for the given {@link String} or null if it cannot be
         * parsed into an enum value.
         */
        public static Direction fromStringOrNull(String value) {
            try {
                return fromString(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public static Direction fromStringDefaultAsc(String value) {
            try {
                return fromString(value);
            } catch (IllegalArgumentException e) {
                return Direction.ASC;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.getSorts() != null) {
            this.getSorts().forEach(x -> {
                sb.append("name=").append(x.getName()).append(",order=").append(x.getDirection().name()).append(";");
            });
        }
        sb.append("page=").append(this.getPage()).append(",").append("size=").append(this.getSize());
        return sb.toString();
    }
}
